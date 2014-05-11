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

    
}

@end
