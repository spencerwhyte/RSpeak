//
//  Cloud.h
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-03-15.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Question.h"
#import "AskQuestionDelegate.h"
#import "AFNetworking.h"
#import "DeviceInformationRequestDelegate.h"

@interface Cloud : NSObject

-(void)askQuestion:(Question *)question delegate:(NSObject<AskQuestionDelegate> *)delegate;

+(Cloud*)sharedInstance;


@end
