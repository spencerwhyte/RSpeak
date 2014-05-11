//
//  Cloud.m
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-03-15.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import "Cloud.h"


static Cloud * sharedInstance = nil;

@implementation Cloud

+(Cloud *)sharedInstance{
    if(sharedInstance == nil){
        sharedInstance = [[Cloud alloc] init];
    }
    return sharedInstance;
}


-(void)askQuestion:(Question *)question delegate:(NSObject<AskQuestionDelegate> *)delegate{
    
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    NSDictionary *parameters = @{@"question": @"Will this work?"};
    [manager POST:@"http://google.com" parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [delegate questionAskDidSucceed];
        NSLog(@"JSON: %@", responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [delegate questionAskDidFail];
        NSLog(@"Error: %@", error);
    }];
    
    
    
}

@end
